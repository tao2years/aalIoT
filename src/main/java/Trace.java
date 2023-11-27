public class Trace {
    private SingleOperation lastOperation;
    private SingleOperation currentOperation;

    public Trace(SingleOperation lastOperation, SingleOperation currentOperation) {
        this.lastOperation = lastOperation;
        this.currentOperation = currentOperation;
    }

    public SingleOperation getLastOperation() {
        return lastOperation;
    }

    public void setLastOperation(SingleOperation lastOperation) {
        this.lastOperation = lastOperation;
    }

    public SingleOperation getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(SingleOperation currentOperation) {
        this.currentOperation = currentOperation;
    }

    @Override
    public String toString() {
        return "{" +
                "\"lastOperation\":" + lastOperation + "," +
                "\"currentOperation\":" + currentOperation +
                "}";
    }

}
