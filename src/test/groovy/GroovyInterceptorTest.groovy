import org.junit.Test

/**
 * @author Administrator
 * @version 1.0 2014/7/14,14:15
 */
class GroovyInterceptorTest {
    class TestObject implements GroovyInterceptable {
        def values() {
            return [1, 2];
        }

        @Override
        Object invokeMethod(String name, Object args) {
            def result =super.invokeMethod(name,args);
            result.add('added');
            return result
        }
    }

    @Test
    public void intercepted() throws Exception {
        assert new TestObject().values() == [1, 2, 'added'];
    }
}
