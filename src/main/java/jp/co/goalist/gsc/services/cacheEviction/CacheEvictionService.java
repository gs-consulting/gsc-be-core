package jp.co.goalist.gsc.services.cacheEviction;

import jakarta.persistence.EntityManager;
import jp.co.goalist.gsc.entities.OemAdvertisement;
import jp.co.goalist.gsc.entities.OemApplicant;
import jp.co.goalist.gsc.entities.OperatorAdvertisement;
import jp.co.goalist.gsc.entities.OperatorApplicant;
import org.hibernate.Session;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CacheEvictionService {

    private final EntityManager entityManager;

    public CacheEvictionService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @CacheEvict(value = "operatorApplicantCache", key = "#applicantId")
    public void evictOperatorApplicantCache(String applicantId) {
        Session session = entityManager.unwrap(Session.class);
        session.getSessionFactory().getCache().evictEntityData(OperatorApplicant.class, applicantId);
    }

    @CacheEvict(value = "oemApplicantCache", key = "#applicantId")
    public void evictOemApplicantCache(String applicantId) {
        Session session = entityManager.unwrap(Session.class);
        session.getSessionFactory().getCache().evictEntityData(OemApplicant.class, applicantId);
    }

    @CacheEvict(value = "operatorAdvertisementsCache", key = "#advId")
    public void evictOperatorAdvertisementCache(String advId) {
        Session session = entityManager.unwrap(Session.class);
        session.getSessionFactory().getCache().evictEntityData(OperatorAdvertisement.class, advId);
    }

    @CacheEvict(value = "oemAdvertisementsCache", key = "#advId")
    public void evictOemAdvertisementCache(String advId) {
        Session session = entityManager.unwrap(Session.class);
        session.getSessionFactory().getCache().evictEntityData(OemAdvertisement.class, advId);
    }
}
