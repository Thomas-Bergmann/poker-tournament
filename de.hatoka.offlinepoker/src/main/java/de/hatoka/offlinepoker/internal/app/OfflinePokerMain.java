package de.hatoka.offlinepoker.internal.app;

public class OfflinePokerMain
{
    public static final void main(String...args) throws Exception
    {
        int servicePort = new Integer(System.getenv("PORT"));
        JettyApplication application = new JettyApplication(servicePort, OfflinePokerApplication.class);
        application.start();
    }
}
