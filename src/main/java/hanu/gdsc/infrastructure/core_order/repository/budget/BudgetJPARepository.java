package hanu.gdsc.infrastructure.core_order.repository.budget;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetJPARepository extends JpaRepository<BudgetEntity, String> {
    BudgetEntity findByCoderId(String coderId);
}
