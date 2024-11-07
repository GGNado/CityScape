package pkg.cityScape.model;

import org.eclipse.sisu.Priority;

public class Board {
    private int id;
    private String title;
    private String description;
    private Priority priority;
    private boolean isPublic;

    public Board(int id, String title, String description, Priority priority, boolean isPublic) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.isPublic = isPublic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
