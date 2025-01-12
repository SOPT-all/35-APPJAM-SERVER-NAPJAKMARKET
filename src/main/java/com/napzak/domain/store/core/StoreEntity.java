package com.napzak.domain.store.core;

import static com.napzak.domain.store.core.StoreTableConstants.*;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
	name = TABLE_STORE,
	indexes = {
		@Index(name = "uk1", columnList = "phone", unique = true)
	}
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_NICKNAME, nullable = true)
	private String nickname;

	@Column(name = COLUMN_PHONE, nullable = true)
	private String phone;

	@Column(name = COLUMN_PHOTO, nullable = true)
	private String photo;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_ROLE, nullable = false, columnDefinition = "varchar(10) default 'Store'")
	private Role role;

	@Column(name = COLUMN_CREATED_AT, nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = COLUMN_DELETED_AT, nullable = true)
	private LocalDateTime deletedAt;

	@Column(name = COLUMN_SOCIAL_ID, nullable = true)
	private Long socialId;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_SOCIAL_TYPE, nullable = true)
	private SocialType socialType;

	@Builder
	private StoreEntity(String nickname, String phone, Role role, Long socialId, SocialType socialType, String photo) {
		this.nickname = nickname;
		this.phone = phone;
		this.photo = photo;
		this.role = role;
		this.socialId = socialId;
		this.socialType = socialType;
	}

	public static StoreEntity create(
		final String nickname,
		final String phone,
		final Role role,
		final Long socialId,
		final SocialType socialType,
		final String photo
	) {
		return StoreEntity.builder()
			.nickname(nickname)
			.phone(phone)
			.role(role)
			.socialId(socialId)
			.socialType(socialType)
			.photo(photo)
			.build();
	}
}
