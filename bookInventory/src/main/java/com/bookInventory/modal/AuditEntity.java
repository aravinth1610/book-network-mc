package com.bookInventory.modal;

import java.util.Date;

import org.slf4j.MDC;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author aravinth
 * @since 2024
 *
 *        A sample source file for the code formatter preview
 */
@Getter
@Setter
@MappedSuperclass
@Slf4j
public class AuditEntity {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedOn;

	@Column(updatable = false)
	private String createdBy;

	private String updatedBy;

	private Integer deleteFlag;

	@PrePersist
	protected void onCreate() {
		this.createdOn = new Date();
		this.deleteFlag = 0;
		String userId = MDC.get("X-User-ID");
		if (userId != null) {
			this.createdBy = userId;
		}else {
			log.warn("MDC does not contain user ID when persisting {}", this.getClass().getSimpleName());
		}
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedOn = new Date();
		String userId = MDC.get("X-User-ID");
		if (userId != null) {
			this.updatedBy = userId;
		}else {
			log.warn("MDC does not contain user ID when persisting {}", this.getClass().getSimpleName());
		}

	}

}