package nl.haltedata.tools;

import java.util.function.Supplier;

import org.springframework.batch.item.ItemProcessor;

public interface ChildItemProcessor<I, O, P> extends ItemProcessor<I, O> {
    Supplier<P> getParentSupplier();
}
