package info.masterfrog.labyrinth.entity;

class EntityIdentifier {
    private String id;
    private String parentId;

    EntityIdentifier(String id, String parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntityIdentifier)) {
            return false;
        }

        EntityIdentifier that = (EntityIdentifier) o;

        if (!id.equals(that.id)) {
            return false;
        }

        return parentId.equals(that.parentId);
    }

    @Override
    public String toString() {
        return "EntityIdentifier{" +
            "id='" + id + '\'' +
            ", parentId='" + parentId + '\'' +
            '}';
    }
}
