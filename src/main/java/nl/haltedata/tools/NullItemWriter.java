package nl.haltedata.tools;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

/**
 * ItemWriter that just ignores everything. For debug purposes
 * @param <T>
 */
public class NullItemWriter<T> implements ItemWriter<T> {

    @Override
    public void write(Chunk<? extends T> chunk) throws Exception {
        // Dummy operation to allow breakPoint
        int i = 0;
    }

}
