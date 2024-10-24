package com.chi.bnbserv.util.csvConverter;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;


public class PriceStringConverter extends AbstractBeanField {
    @Override
    protected String convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        String handledString = "0.00";
        if(StringUtils.isNotEmpty(s) && s.startsWith("$")) {
            handledString = s.replace("$", "");
            handledString = handledString.replaceAll(",", "");
        }
        // return new BigDecimal(handledString);
        return handledString;
    }
}