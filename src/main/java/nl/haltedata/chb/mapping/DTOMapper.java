package nl.haltedata.chb.mapping;

public interface DTOMapper<T1, T2, T3> {

    public T3 map(T1 entity, T2 parent);

}
