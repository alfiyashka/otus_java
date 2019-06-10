package atm.impl;

import atm.*;
import atm.impl.atms.AbstractAtm;
import atm.impl.atms.CashInCashOutAtm;
import atm.impl.atms.CashOutAtm;
import atm.impl.atms.RecycleAtm;

import java.util.ArrayList;
import java.util.List;

// builder, observer, visitor patterns
public class Department implements IDepartment, IObservable, IAtmVisitor {
    private List<AbstractAtm> atms = new ArrayList();
    private String departmentName;
    private Department() {
    }

    public String departmentName() {
        return departmentName;
    }

    private void setDepartmentName(String name) {
        departmentName = name;
    }

    public int balance () {
        return atms.stream().mapToInt(it -> visitAtmBalance(it)).sum();
    }

    public void addAtms(List<? extends AbstractAtm> atms){
        this.atms.addAll(atms);
    }

    public static class Builder {
        private final List<AbstractAtm> cashOutAtms = new ArrayList<>();
        private final List<AbstractAtm> cashInCashOutAtms = new ArrayList<>();
        private final List<AbstractAtm> recycleAtms = new ArrayList<>();
        private String name;


        public Builder() {
        }

        public Builder withCashOutAtms (List<CashOutAtm> atms) {
            cashOutAtms.addAll(atms);
            return this;
        }

        public Builder withCashInCachOutAtms (List<CashInCashOutAtm> atms) {
            cashInCashOutAtms.addAll(atms);
            return this;
        }

        public Builder withRecycleAtms (List<RecycleAtm> atms) {
            recycleAtms.addAll(atms);
            return this;
        }

        public Builder withDepartmentName (String name) {
            this.name = name;
            return this;
        }

        public Department build() {

            Department department = new Department();

            department.addAtms(cashOutAtms);
            department.addAtms(cashInCashOutAtms);
            department.addAtms(recycleAtms);
            department.saveStateAtms();

            department.setDepartmentName(name);
            return department;
        }
    }

    @Override
    public void undoStateAtms() {
        for(IObserver atm: atms) {
            atm.undoState();
        }
    }

    @Override
    public void saveStateAtms() {
        for (IObserver atm: atms) {
            atm.saveState();
        }
    }

    @Override
    public int visitAtmBalance(AbstractAtm atm) {
        return atm.balance();
    }
}
