
    @Test
public void testByteSizeParsing() {
    Bytesize b = new Bytesize("1.5MB");
    assertEquals(1.5 * 1024 * 1024, b.getBytes(), 0.1);
}

