package hanu.gdsc.domain.core_like.models;

import hanu.gdsc.domain.share.models.Id;
import hanu.gdsc.domain.share.models.IdentitifedVersioningDomainObject;

public class ReactedObject extends IdentitifedVersioningDomainObject{
    private long likeCount;
    private long dislikeCount;
    private String serviceToCreate;

    private ReactedObject(Id id, long version, long likeCount, long dislikeCount, String serviceToCreate) {
        super(id, version);
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.serviceToCreate = serviceToCreate;
    }

    public static ReactedObject create(String serviceToCreate) {
        return new ReactedObject(
            Id.generateRandom(),
            0,
            0,
            0,
            serviceToCreate);
    }

    public void increaseLikeCount() {
        this.likeCount += 1;
    }

    public void decreaseLikeCount() {
        if(likeCount >= 1) {
            this.likeCount -= 1;
        }
    }

    public void increaseDislikeCount() {
        this.dislikeCount += 1;
    }

    public void decreaseDislikeCount() {
        if(dislikeCount >= 1) {
            this.dislikeCount -= 1;
        }
    }


    public long getLikeCount() {
        return likeCount;
    }

    public long getDislikeCount() {
        return dislikeCount;
    }

    public String getServiceToCreate() {
        return serviceToCreate;
    }
}
