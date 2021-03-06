package answer.king.repo;


import answer.king.config.AppConfig;
import answer.king.model.Reciept;
import answer.king.throwables.exception.AnswerKingException;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.math.BigDecimal;

import static answer.king.util.ModelUtil.createBurgerOrder;
import static answer.king.util.ModelUtil.createReciept;
import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
                          TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class} )
public class RecieptRepositoryTest {

    protected static final String ORDER_DATASET = "/order-dataset.xml";
    protected static final String RECIEPT_DATASET = "/reciept-dataset.xml";

    private final Long RECIEPT_ID = 1L;
    private final BigDecimal PAYMENT = new BigDecimal("10.00");
    private final BigDecimal CHANGE = new BigDecimal("8.01");

    @Autowired
    private RecieptRepository recieptRepository;


    @Test
    @DatabaseSetup(value = ORDER_DATASET, type = DatabaseOperation.CLEAN_INSERT)
    public void saveTest() {
        //setup
        Long orderId = 1L;
        Reciept reciept = createReciept(null, createBurgerOrder(orderId), PAYMENT);

        // execution
        Reciept result = recieptRepository.save(reciept);

        // verification
        assertEquals(RECIEPT_ID, result.getId());
        assertEquals(orderId, result.getOrder().getId());
        assertEquals(PAYMENT, result.getPayment());
        assertEquals(CHANGE, result.getChange());
    }

    @Test
    @DatabaseSetup(value = {ORDER_DATASET, RECIEPT_DATASET})
    public void findOneTest() {
        // execution
        Reciept result = recieptRepository.findOne(RECIEPT_ID);

        // verification
        assertEquals(RECIEPT_ID, result.getId());
        assertEquals(PAYMENT, result.getPayment());
        assertEquals(CHANGE, result.getChange());
    }
}
