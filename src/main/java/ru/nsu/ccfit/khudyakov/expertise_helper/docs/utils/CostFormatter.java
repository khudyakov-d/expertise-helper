package ru.nsu.ccfit.khudyakov.expertise_helper.docs.utils;

import com.github.moneytostr.MoneyToStr;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.github.moneytostr.MoneyToStr.Currency;
import static com.github.moneytostr.MoneyToStr.Language;
import static com.github.moneytostr.MoneyToStr.Pennies;

public class CostFormatter {

    private static final NumberFormat formatter = new DecimalFormat("#0.00");

    private static final MoneyToStr costFormatter = new MoneyToStr(Currency.RUR, Language.RUS, Pennies.NUMBER);

    public static String formatCost(double cost) {
        return formatter.format(cost);
    }

    private static String formatCostInBrackets(double cost) {
        return String.format("(%,.2f руб)", cost);
    }

    public static String formatCursiveCost(double cost) {
        String costInBrackets = formatCostInBrackets(cost);
        return costInBrackets + " " + StringUtils.capitalize(costFormatter.convert(cost));
    }


    public static String formatContractorInitials(String contractorName) {
        String[] words = contractorName.split(" ");
        if (words.length == 2) {
            return words[0] + " " + words[1].charAt(0) + ".";
        }

        return words[0] + " " + words[1].charAt(0) + ". " + words[2].charAt(0) + ".";
    }

}
