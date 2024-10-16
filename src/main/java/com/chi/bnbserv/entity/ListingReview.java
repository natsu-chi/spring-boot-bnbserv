package com.chi.bnbserv.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "listing_review")
public class ListingReview {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    // 關聯 Listing (一對一)
    @OneToOne
    @MapsId // 使用相同的主鍵
    @JoinColumn(name = "id")
    private Listing listing;

    @Column(name = "number_of_reviews_ltm")
    private Integer numberOfReviewsLtm;

    @Column(name = "number_of_reviews_l30d")
    private Integer numberOfReviewsL30d;

    @Column(name = "reviews_per_month")
    private Integer reviewsPerMonth;

    @Column(name = "review_scores_rating", precision = 3, scale = 2)
    private BigDecimal reviewScoresRating;

    @Column(name = "review_scores_accuracy", precision = 3, scale = 2)
    private BigDecimal reviewScoresAccuracy;

    @Column(name = "review_scores_cleanliness", precision = 3, scale = 2)
    private BigDecimal reviewScoresCleanliness;

    @Column(name = "review_scores_checkin", precision = 3, scale = 2)
    private BigDecimal reviewScoresCheckin;

    @Column(name = "review_scores_communication", precision = 3, scale = 2)
    private BigDecimal reviewScoresCommunication;

    @Column(name = "review_scores_location", precision = 3, scale = 2)
    private BigDecimal reviewScoresLocation;

    @Column(name = "review_scores_value", precision = 3, scale = 2)
    private BigDecimal reviewScoresValue;

    @Column(name = "first_review")
    private LocalDate firstReview;

    @Column(name = "last_review")
    private LocalDate lastReview;
}
