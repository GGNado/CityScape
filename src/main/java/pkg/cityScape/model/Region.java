package pkg.cityScape.model;

import pkg.cityScape.enums.ChunkPermission;

import java.util.Map;

public class Region {
    private String id; // x:y
    private int fk_town;
    private Integer x;
    private Integer y;
    private Integer z;
    private Citizen citizenOwner;
    private Map<ChunkPermission, Boolean> chunkPermission;

    public Region(String id, int fk_town, Integer x, Integer y, Integer z, Citizen citizenOwner, Map<ChunkPermission, Boolean> chunkPermission) {
        this.id = id;
        this.fk_town = fk_town;
        this.x = x;
        this.y = y;
        this.z = z;
        this.citizenOwner = citizenOwner;
        this.chunkPermission = chunkPermission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFk_town() {
        return fk_town;
    }

    public void setFk_town(int fk_town) {
        this.fk_town = fk_town;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public Citizen getCitizenOwner() {
        return citizenOwner;
    }

    public void setCitizenOwner(Citizen citizenOwner) {
        this.citizenOwner = citizenOwner;
    }

    public Map<ChunkPermission, Boolean> getChunkPermission() {
        return chunkPermission;
    }

    public void setChunkPermission(Map<ChunkPermission, Boolean> chunkPermission) {
        this.chunkPermission = chunkPermission;
    }
}
