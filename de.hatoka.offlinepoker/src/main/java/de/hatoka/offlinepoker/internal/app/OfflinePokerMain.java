package de.hatoka.offlinepoker.internal.app;

import de.hatoka.common.capi.configuration.SystemPropertyProvider;
import de.hatoka.common.internal.configuration.SystemPropertyProviderImpl;

public class OfflinePokerMain
{
    private static final SystemPropertyProvider system = new SystemPropertyProviderImpl();
    public static final void main(String...args) throws Exception
    {
        int servicePort = new Integer(system.get("PORT"));
        JettyApplication application = new JettyApplication(servicePort, OfflinePokerApplication.class);
        application.start();
    }
}
