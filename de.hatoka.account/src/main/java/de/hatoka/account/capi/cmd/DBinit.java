package de.hatoka.account.capi.cmd;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import de.hatoka.account.capi.entities.AccountPO;
import de.hatoka.account.capi.entities.UserPO;

public class DBinit
{
    public static void main(String[] args)
    {
        Configuration config = new Configuration();
        config.addAnnotatedClass(AccountPO.class);
        config.addAnnotatedClass(UserPO.class);
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        SchemaExport schema = new SchemaExport(config);
        schema.create(true, false);
    }

    private DBinit()
    {
    }
}
