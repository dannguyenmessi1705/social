package com.didan.social.entity;

import com.didan.social.entity.keys.FollowerId;
import jakarta.persistence.*;


@Entity(name = "followers")
public class Followers {
    @EmbeddedId
    FollowerId folId;

    @ManyToOne
    @JoinColumn(name = "follower_id", insertable = false, updatable = false)
    private Users users1;

    @ManyToOne
    @JoinColumn(name = "followed_id", insertable = false, updatable = false)
    private Users users2;

    public Followers(){}

    public Followers(FollowerId folId, Users users1, Users users2) {
        this.folId = folId;
        this.users1 = users1;
        this.users2 = users2;
    }

    public FollowerId getFolId() {
        return folId;
    }

    public void setFolId(FollowerId folId) {
        this.folId = folId;
    }

    public Users getUsers1() {
        return users1;
    }

    public void setUsers1(Users users1) {
        this.users1 = users1;
    }

    public Users getUsers2() {
        return users2;
    }

    public void setUsers2(Users users2) {
        this.users2 = users2;
    }

    // others fields, getters and setters
}
