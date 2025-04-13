package com.hsbc.zmx.transaction.service.supervisor;

import com.hsbc.zmx.transaction.entity.TransactionEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class SuperviseService {

    List<SuperviseInterface> supervisors;

    @Autowired
    ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        // 获取所有SuperviseInterface实现
        Map<String, SuperviseInterface> superviseInterfaceMap = applicationContext.getBeansOfType(SuperviseInterface.class);
        supervisors = superviseInterfaceMap.values().stream().sorted(
                Comparator.comparingInt(SuperviseInterface::getPriority)
        ).toList();
    }

    public SupervisionResult supervise(TransactionEntity transaction) {
        for (SuperviseInterface supervisor : supervisors) {
            SupervisionResult result = supervisor.supervise(transaction);
            if (!result.getSuccess()) {
                return result;
            }
        }
        return new SupervisionResult();
    }
}
