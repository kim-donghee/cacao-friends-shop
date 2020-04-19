package cacao.friends.shop.modules.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import cacao.friends.shop.modules.account.Account;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	Notification findByAccountAndChecked(Account currentAccount, boolean checked);

	Long countByAccountAndChecked(Account currentAccount, boolean checked);

}
