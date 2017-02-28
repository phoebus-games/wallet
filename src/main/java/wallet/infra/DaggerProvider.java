package wallet.infra;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import dagger.ObjectGraph;
import wallet.Config;

import javax.annotation.Resource;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;

@Provider
public class DaggerProvider implements InjectableProvider<Resource, Type> {

    private final ObjectGraph objectGraph = ObjectGraph.create(new Config());

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable getInjectable(ComponentContext ic, Resource resource, Type type) {
        return () -> {
            try {
                return objectGraph.get(Class.forName(type.getTypeName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
