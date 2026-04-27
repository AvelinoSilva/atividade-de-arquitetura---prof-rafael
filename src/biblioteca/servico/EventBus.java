package biblioteca.servico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {
    private Map<Class<?>, List<Consumer<?>>> subscribers = new HashMap<>();

    public <T> void assinar(Class<T> tipoEvento, Consumer<T> consumidor) {
        subscribers.computeIfAbsent(tipoEvento, k -> new ArrayList<>())
                   .add(consumidor);
    }

    @SuppressWarnings("unchecked")
    public <T> void publicar(T evento) {
        Class<?> tipoEvento = evento.getClass();
        List<Consumer<?>> consumidores = subscribers.get(tipoEvento);
        
        if (consumidores != null) {
            for (Consumer<?> consumidor : consumidores) {
                ((Consumer<T>) consumidor).accept(evento);
            }
        }
    }
}
