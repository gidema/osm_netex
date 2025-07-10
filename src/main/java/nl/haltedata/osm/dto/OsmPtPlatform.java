package nl.haltedata.osm.dto;

public interface OsmPtPlatform {

    Long getOsmPrimitiveId();

    void setOsmPrimitiveId(Long osmPrimitiveId);

    String getPrimitiveType();

    void setPrimitiveType(String primitiveType);

    String getName();

    void setName(String name);

    Boolean getIsBus();

    void setIsBus(Boolean isBus);

    String getQuayCode();

    void setQuayCode(String quayCode);

    String getAreaCode();

    void setAreaCode(String areaCode);

    String getStopSideCode();

    void setStopSideCode(String stopSideCode);

    String getNote();

    void setNote(String note);

    String toString();

}