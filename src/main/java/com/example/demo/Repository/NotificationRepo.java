package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Model.Notification;

public interface NotificationRepo extends JpaRepository<Notification,Long> {
}
