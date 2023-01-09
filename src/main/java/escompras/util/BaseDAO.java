package escompras.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public abstract class BaseDAO
{
	private static StandardServiceRegistry ssr;
	private static SessionFactory sFactory;
	protected interface DAOTransaction<R> { R Persist(Session session); }
	
	private static SessionFactory getSessionFactory()
	{
		if (sFactory == null) {
			try {
				ssr = (new StandardServiceRegistryBuilder()).configure("hibernate.cfg.xml").build();
				sFactory = (new MetadataSources(ssr)).getMetadataBuilder().build()
					.getSessionFactoryBuilder().build();
			} catch (Exception e) {
				System.out.println("FactoryException: " + e.getMessage());
				if (ssr != null)
					StandardServiceRegistryBuilder.destroy(ssr);
			}
		}
		return sFactory;
	}
	
	protected static <T> T DoTransaction(DAOTransaction<T> dAOTransaction, String msg)
	{
		T returnValue = null;
		Session s = getSessionFactory().getCurrentSession();
		Transaction t = s.getTransaction();
		try {
			t.begin();
			returnValue = dAOTransaction.Persist(s);
			t.commit();
		} catch (HibernateException e) {
			System.out.println(msg + "DAOException: " + e);
			if (t.isActive())
				t.rollback();
		}
		return returnValue;
	}
}
