import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import play.Application;
import play.GlobalSettings;

/**
 * Application wide behaviour. We establish a Spring application context for the dependency injection system and
 * configure Spring Data.
 */
@SuppressWarnings("unused")
public class Global extends GlobalSettings
{
    private ApplicationContext ctx;

    /**
     * Sync the context lifecycle with Play's.
     */
    @Override
    public void onStart(final Application app)
    {
        super.onStart(app);

        // AnnotationConfigApplicationContext can only be refreshed once, but we do it here even though this method
        // can be called multiple times. The reason for doing during startup is so that the Play configuration is
        // entirely available to this application context.
        ctx = new ClassPathXmlApplicationContext("beans.xml");
    }

    /**
     * Controllers must be resolved through the application context. There is a special method of GlobalSettings
     * that we can override to resolve a given controller. This resolution is required by the Play router.
     */
    @Override
    public <A> A getControllerInstance(Class<A> aClass)
    {
        return ctx.getBean(aClass);
    }
}
