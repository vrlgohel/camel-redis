package org.apache.camel.component.redis;

import java.util.HashSet;
import java.util.Set;

import org.apache.camel.impl.JndiRegistry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedisSetTest extends RedisTestSupport {
    private RedisTemplate redisTemplate;
    private SetOperations setOperations;

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        when(redisTemplate.opsForSet()).thenReturn(setOperations);

        JndiRegistry registry = super.createRegistry();
        registry.bind("redisTemplate", redisTemplate);
        return registry;
    }

    @Before
    public void setUp() throws Exception {
        redisTemplate = mock(RedisTemplate.class);
        setOperations = mock(SetOperations.class);
        super.setUp();
    }

    @Test
    public void shouldExecuteSADD() throws Exception {
        when(setOperations.add(anyString(), anyObject())).thenReturn(false);

        Object result = sendHeaders(
                RedisConstants.COMMAND, "SADD",
                RedisConstants.KEY, "key",
                RedisConstants.VALUE, "value");

        verify(setOperations).add("key", "value");
        assertEquals(false, result);

    }

    @Test
    public void shouldExecuteSCARD() throws Exception {
        when(setOperations.size(anyString())).thenReturn(2L);

        Object result = sendHeaders(
                RedisConstants.COMMAND, "SCARD",
                RedisConstants.KEY, "key");

        verify(setOperations).size("key");
        assertEquals(2L, result);
    }

    @Test
    public void shouldExecuteSDIFF() throws Exception {
        Set<String> difference = new HashSet<String>();
        difference.add("a");
        difference.add("b");
        when(setOperations.difference(anyString(), anyCollection())).thenReturn(difference);

        Set<String> keys = new HashSet<String>();
        keys.add("key2");
        keys.add("key3");
        Object result = sendHeaders(
                RedisConstants.COMMAND, "SDIFF",
                RedisConstants.KEY, "key",
                RedisConstants.KEYS, keys);

        verify(setOperations).difference("key", keys);
        assertEquals(difference, result);
    }

    @Test
    public void shouldExecuteSDIFFSTORE() throws Exception {
        Set<String> keys = new HashSet<String>();
        keys.add("key2");
        keys.add("key3");
        Object result = sendHeaders(
                RedisConstants.COMMAND, "SDIFFSTORE",
                RedisConstants.KEY, "key",
                RedisConstants.KEYS, keys,
                RedisConstants.DESTINATION, "destination");

        verify(setOperations).differenceAndStore("key", keys, "destination");
    }

    @Test
    public void shouldExecuteSINTER() throws Exception {
        Set<String> difference = new HashSet<String>();
        difference.add("a");
        difference.add("b");
        when(setOperations.intersect(anyString(), anyCollection())).thenReturn(difference);

        Set<String> keys = new HashSet<String>();
        keys.add("key2");
        keys.add("key3");
        Object result = sendHeaders(
                RedisConstants.COMMAND, "SINTER",
                RedisConstants.KEY, "key",
                RedisConstants.KEYS, keys);

        verify(setOperations).intersect("key", keys);
        assertEquals(difference, result);
    }

    @Test
    public void shouldExecuteSINTERSTORE() throws Exception {
        Set<String> keys = new HashSet<String>();
        keys.add("key2");
        keys.add("key3");
        Object result = sendHeaders(
                RedisConstants.COMMAND, "SINTERSTORE",
                RedisConstants.KEY, "key",
                RedisConstants.DESTINATION, "destination",
                RedisConstants.KEYS, keys);

        verify(setOperations).intersectAndStore("key", keys, "destination");
    }

    @Test
    public void shouldExecuteSISMEMBER() throws Exception {
        when(setOperations.isMember(anyString(), anyObject())).thenReturn(true);

        Object result = sendHeaders(
                RedisConstants.COMMAND, "SISMEMBER",
                RedisConstants.KEY, "key",
                RedisConstants.VALUE, "set");

        verify(setOperations).isMember("key", "set");
        assertEquals(true, result);
    }

    @Test
    public void shouldExecuteSMEMBERS() throws Exception {
        Set<String> keys = new HashSet<String>();
        keys.add("key2");
        keys.add("key3");

        when(setOperations.members(anyString())).thenReturn(keys);

        Object result = sendHeaders(
                RedisConstants.COMMAND, "SMEMBERS",
                RedisConstants.KEY, "key");

        verify(setOperations).members("key");
        assertEquals(keys, result);
    }

    @Test
    public void shouldExecuteSMOVE() throws Exception {
        Object result = sendHeaders(
                RedisConstants.COMMAND, "SMOVE",
                RedisConstants.KEY, "key",
                RedisConstants.VALUE, "value",
                RedisConstants.DESTINATION, "destination");

        verify(setOperations).move("key", "value", "destination");
    }

    @Test
    public void shouldExecuteSPOP() throws Exception {
        String field = "value";
        when(setOperations.pop(anyString())).thenReturn(field);

        Object result = sendHeaders(
                RedisConstants.COMMAND, "SPOP",
                RedisConstants.KEY, "key");

        verify(setOperations).pop("key");
        assertEquals(field, result);
    }

    @Test
    public void shouldExecuteSRANDMEMBER() throws Exception {
        String field = "value";
        when(setOperations.randomMember(anyString())).thenReturn(field);

        Object result = sendHeaders(
                RedisConstants.COMMAND, "SRANDMEMBER",
                RedisConstants.KEY, "key");

        verify(setOperations).randomMember("key");
        assertEquals(field, result);
    }

    @Test
    public void shouldExecuteSREM() throws Exception {
        when(setOperations.remove(anyString(), anyObject())).thenReturn(true);

        Object result = sendHeaders(
                RedisConstants.COMMAND, "SREM",
                RedisConstants.KEY, "key",
                RedisConstants.VALUE, "value");

        verify(setOperations).remove("key", "value");
        assertEquals(true, result);
    }

    @Test
    public void shouldExecuteSUNION() throws Exception {
        Set<String> resultKeys = new HashSet<String>();
        resultKeys.add("key2");
        resultKeys.add("key3");

        when(setOperations.union(anyString(), anyCollection())).thenReturn(resultKeys);

        Set<String> keys = new HashSet<String>();
        keys.add("key2");
        keys.add("key4");

        Object result = sendHeaders(
                RedisConstants.COMMAND, "SUNION",
                RedisConstants.KEY, "key",
                RedisConstants.KEYS, keys);

        verify(setOperations).union("key", keys);
        assertEquals(resultKeys, result);
    }

    @Test
    public void shouldExecuteSUNIONSTORE() throws Exception {
        Set<String> keys = new HashSet<String>();
        keys.add("key2");
        keys.add("key4");

        Object result = sendHeaders(
                RedisConstants.COMMAND, "SUNIONSTORE",
                RedisConstants.KEY, "key",
                RedisConstants.KEYS, keys,
                RedisConstants.DESTINATION, "destination");

        verify(setOperations).unionAndStore("key", keys, "destination");
    }
}
