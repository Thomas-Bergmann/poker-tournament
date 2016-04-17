package de.hatoka.common.capi.app.xslt;

import java.text.SimpleDateFormat;
import java.time.Duration;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.common.capi.resource.LocalizationConstants;

public class DurationXmlAdapter extends XmlAdapter<String, Duration>
{
    private final SimpleDateFormat dateFormat;

    public DurationXmlAdapter()
    {
        dateFormat = new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT_SECONDS);
        dateFormat.setTimeZone(CountryHelper.UTC);
    }

    @Override
    public String marshal(Duration v) throws Exception
    {
        if (v == null)
        {
            return null;
        }
        long fullSeconds = v.getSeconds();
        long minutes = fullSeconds / 60;
        long seconds = fullSeconds % 60;
        return (minutes < 10 ? "0" : "") + Long.valueOf(minutes) + ":" + (seconds < 10 ? "0" : "") + Long.valueOf(seconds);
    }

    @Override
    public Duration unmarshal(String v) throws Exception
    {
        if (v == null)
        {
            return null;
        }
        String[] minutesAndSeconds = v.split(":");
        return Duration.ofSeconds(Long.valueOf(minutesAndSeconds[0]) * 60 + Long.valueOf(minutesAndSeconds[1]));
    }

}