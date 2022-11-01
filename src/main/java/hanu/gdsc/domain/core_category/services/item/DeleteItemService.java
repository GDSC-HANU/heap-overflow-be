package hanu.gdsc.domain.core_category.services.item;

import hanu.gdsc.domain.share.models.Id;

import java.util.List;

public interface DeleteItemService {

    public void deleteById(Id id, String serviceToCreate);

    public void deleteMany(List<Id> categoryIds);

}
