package com.bhavani.service;

import com.bhavani.model.PlanType;
import com.bhavani.model.Subscription;
import com.bhavani.model.User;

public interface SubscriptionService {

    Subscription createSubscription(User user);

    Subscription getUsersSubscription(Long userId)throws Exception;

    Subscription upgradeSubscription(Long userId, PlanType planType)throws Exception;

    boolean isValid(Subscription subscription);

}
