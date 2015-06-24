package de.hatoka.common.capi.app.xslt;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.common.capi.resource.LocalizationConstants;

public class DateXmlAdapter extends XmlAdapter<String, Date> {

    private final SimpleDateFormat dateFormat;

    public DateXmlAdapter()
    {
        dateFormat = new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT_SECONDS);
        dateFormat.setTimeZone(CountryHelper.UTC);
    }
    @Override
    public String marshal(Date v) throws Exception {
        return dateFormat.format(v);
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        return dateFormat.parse(v);
    }

}