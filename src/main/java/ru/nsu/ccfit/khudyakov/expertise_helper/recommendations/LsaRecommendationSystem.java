package ru.nsu.ccfit.khudyakov.expertise_helper.recommendations;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.khudyakov.expertise_helper.recommendations.model.Doc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;

@Component
@Scope(value="prototype", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class LsaRecommendationSystem {

    private final Map<String, Integer> termsMap = new HashMap<>();

    private final Map<UUID, Integer> docsMap = new HashMap<>();

    private void prepareTerms(List<Doc> docs) {
        Set<String> terms = new HashSet<>();

        for (int i = 0, docsSize = docs.size(); i < docsSize; ++i) {
            Doc doc = docs.get(i);
            if (doc.getDocTerms() != null) {
                terms.addAll(doc.getDocTerms());
            }
            docsMap.put(doc.getID(), i);
        }

        int i = 0;
        for (String term : terms) {
            termsMap.put(term, i++);
        }
    }

    private void calcDocTF(Doc doc, RealMatrix matrix) {
        if (doc.getDocTerms() != null) {
            int i = docsMap.get(doc.getID());
            List<String> terms = doc.getDocTerms();

            for (String term : terms) {
                int j = termsMap.get(term);
                matrix.setEntry(i, j, matrix.getEntry(i, j) + 1);
            }

            for (String term : terms) {
                int j = termsMap.get(term);
                matrix.setEntry(i, j, matrix.getEntry(i, j) / terms.size());
            }
        }
    }

    private void calcTF(List<Doc> docs, RealMatrix matrix) {
        for (Doc doc : docs) {
            calcDocTF(doc, matrix);
        }
    }

    private RealVector calcIDF(List<Doc> docs, RealMatrix matrix) {
        RealVector vector = new ArrayRealVector(termsMap.size());

        for (Map.Entry<String, Integer> termsEntry : termsMap.entrySet()) {
            int j = termsEntry.getValue();
            int count = 0;
            for (int i = 0, size = docsMap.size(); i < size; ++i) {
                count = matrix.getEntry(i, j) > 0 ? count + 1 : count;
            }
            vector.setEntry(j, Math.log(docs.size() / (double) count));
        }

        return vector;
    }

    private void calcTFIDF(List<Doc> docs, RealMatrix matrix) {
        calcTF(docs, matrix);
        RealVector idfVector = calcIDF(docs, matrix);

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            matrix.setRowVector(i, matrix.getRowVector(i).ebeMultiply(idfVector));
        }
    }

    private SingularValueDecomposition decompose(List<Doc> docs) {
        RealMatrix matrix = new Array2DRowRealMatrix(docs.size(), termsMap.size());
        calcTFIDF(docs, matrix);
        return new SingularValueDecomposition(matrix);
    }

    private RealMatrix reduceMatrixFactors(SingularValueDecomposition decomposition) {
        double[] singularValues = decomposition.getSingularValues();
        int k = (int) Arrays.stream(singularValues).filter(s -> s > 1).count();
        if (k == 0) {
            k = singularValues.length;
        }

        RealMatrix U = decomposition.getU();
        RealMatrix newU = U.getSubMatrix(0, U.getRowDimension() - 1, 0, k - 1);

        RealMatrix S = decomposition.getS();
        RealMatrix newS = S.getSubMatrix(0, k - 1, 0, k - 1);

        RealMatrix VT = decomposition.getVT();
        RealMatrix newVT = VT.getSubMatrix(0, k - 1, 0, VT.getColumnDimension() - 1);

        return newU.multiply(newS).multiply(newVT);
    }

    private List<UUID> sortResult(Map<UUID, Double> results) {
        List<UUID> sortedResults = new ArrayList<>();
        results.entrySet().stream()
                .sorted(comparingByValue(reverseOrder()))
                .forEachOrdered(v -> {
                    sortedResults.add(v.getKey());
                });

        return sortedResults;
    }

    public List<UUID> calcDocsOrder(List<Doc> docs, UUID targetDocId) {
        prepareTerms(docs);
        SingularValueDecomposition decomposition = decompose(docs);
        RealMatrix matrix = reduceMatrixFactors(decomposition);

        Map<UUID, Double> docsSimilarity = new HashMap<>();
        RealVector targetDocVector = matrix.getRowVector(docsMap.get(targetDocId));

        for (Map.Entry<UUID, Integer> docsEntry : docsMap.entrySet()) {
            if (docsEntry.getKey() != targetDocId) {
                double sim;
                try {
                    sim = matrix.getRowVector(docsEntry.getValue()).cosine(targetDocVector);
                } catch (MathArithmeticException e) {
                    sim = 0;
                }
                docsSimilarity.put(docsEntry.getKey(), sim);
            }
        }
        return sortResult(docsSimilarity);
    }


}
