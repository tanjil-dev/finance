package finance.progoti.repository;
import finance.progoti.user.model.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository  extends JpaRepository<Product, Long>{
//	List<Product> findAllBySubmittedAndApprovedby(final String submittedBy,List<Product> list);
	List<Product> findAllBySubmittedby(final String submittedBy);
	List<Product> findAllByApprovedby(final String approvedBy);
	List<Product> findAllByPaidby(final String paidBy);
	List<Product> findAllById(Long id);
	//List<Product> findAll();
	//List<Product> listAllProduct();
}
