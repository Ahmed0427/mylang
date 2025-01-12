class ReturnVal extends RuntimeException {
    final Object value;

    ReturnVal(Object value) {
        super(null, null, false, false);
        this.value = value;
    }
}
