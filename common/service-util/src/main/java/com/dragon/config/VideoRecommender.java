package com.dragon.config;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class VideoRecommender {
    private final static int NEIGHBORHOOD_NUM = 3;
    @Resource(name = "mySQLDataModel")
    private DataModel dataModel;

    private List<Integer> getRecommendedItemIDs(List<RecommendedItem> recommendations){
        List<Integer> recommendItems = new ArrayList<>();
        for(int i = 0 ; i < recommendations.size() ; i++) {
            RecommendedItem recommendedItem=recommendations.get(i);
            recommendItems.add((int) recommendedItem.getItemID());
        }
        return recommendItems;
    }

    // 基于用户的推荐算法
    public List<Integer> userBasedRecommender(Integer userID,int size) throws TasteException {
        UserSimilarity similarity  = new EuclideanDistanceSimilarity(dataModel);
        NearestNUserNeighborhood neighbor = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, similarity, dataModel);
        Recommender recommender = new CachingRecommender(new GenericUserBasedRecommender(dataModel,neighbor,similarity));
        List<RecommendedItem> recommendations = recommender.recommend(userID, size);
        return getRecommendedItemIDs(recommendations);
    }

    // 基于内容的推荐算法
    public List<Integer> itemBasedRecommender(Integer userID,int size) throws TasteException {
        ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
        Recommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
        List<RecommendedItem> recommendations = recommender.recommend(userID, size);
        return getRecommendedItemIDs(recommendations);
    }
}
