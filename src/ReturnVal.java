class ReturnVal extends RuntimeException {
    final Object value;
    final Token keyword;

    ReturnVal(Object value, Token keyword) {
        super(null, null, false, false);
        this.value = value;
        this.keyword = keyword;
    }
}
