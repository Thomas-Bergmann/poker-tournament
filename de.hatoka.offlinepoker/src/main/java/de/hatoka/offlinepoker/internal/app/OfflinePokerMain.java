package de.hatoka.offlinepoker.internal.app;

public class OfflinePokerMain
{
    private static int servicePort = 80;

    public static final void main(String...args) throws Exception
    {
        JettyApplication application = new JettyApplication(servicePort, OfflinePokerApplication.class);
        application.start();
    }
}
