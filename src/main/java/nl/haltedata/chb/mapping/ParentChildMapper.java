package nl.haltedata.chb.mapping;

/**
 * Map an entity to an other type with the possibility to read data from
 * a parent entity.
 * 
 * @param <I> Input item
 * @param <O> Output item
 * @param <P> Parent item
 */
public interface ParentChildMapper<I, O, P> {

    public O map(I entity, P parent);

}
