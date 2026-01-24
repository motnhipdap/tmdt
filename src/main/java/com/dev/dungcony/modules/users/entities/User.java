// package com.dev.dungcony.modules.users.entities;

// import java.time.LocalDateTime;

// import com.dev.dungcony.modules.authorization.entities.Account;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.OneToOne;
// import jakarta.persistence.PrePersist;
// import jakarta.persistence.PreUpdate;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "tbl_users")
// public class User {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;

// @Column(name = "name")
// private String name;

// @Column(name = "addr")
// private String addr;

// @Column(name = "point")
// private int point;

// @Column(name = "img")
// private String img;

// @OneToOne
// @JoinColumn(name = "acc_id")
// private Account acc;

// @Column(name = "created_at")
// private LocalDateTime createdAt;
// @Column(name = "catalog_id")
// private Long catalogId;

// @Column(name = "updated_at")
// private LocalDateTime updatedAt;

// @PrePersist
// protected void onCreate() {
// this.createdAt = LocalDateTime.now();
// this.updatedAt = LocalDateTime.now();
// }

// @PreUpdate
// protected void onUpdate() {
// this.updatedAt = LocalDateTime.now();
// }

// public Long getId() {
// return id;
// }

// public String getName() {
// return name;
// }

// public String getAddr() {
// return addr;
// }

// public int getPoint() {
// return point;
// }

// public String getImg() {
// return img;
// }

// public LocalDateTime getCreatedAt() {
// return createdAt;
// }

// public LocalDateTime getUpdatedAt() {
// return updatedAt;
// }

// public Long getCatalogId() {
// return catalogId;
// }

// public void setId(Long id) {
// this.id = id;
// }

// public void setName(String name) {
// this.name = name;
// }

// public void setAddr(String addr) {
// this.addr = addr;
// }

// public void setPoint(int point) {
// this.point = point;
// }

// public void setAcc(Account acc) {
// this.acc = acc;
// }

// public void setImg(String img) {
// this.img = img;
// }

// public void setCreatedAt(LocalDateTime createdAt) {
// this.createdAt = createdAt;
// }

// public void setUpdatedAt(LocalDateTime updatedAt) {
// this.updatedAt = updatedAt;
// }

// public void setCatalogId(Long catalogId) {
// this.catalogId = catalogId;
// }
// }
