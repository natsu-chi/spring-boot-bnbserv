package com.chi.bnbserv.util.csvConverter;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class NonEmptyStringConverter extends AbstractBeanField {
    @Override
    protected String convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return StringUtils.isNotEmpty(s) ? s : null;
    }
}
