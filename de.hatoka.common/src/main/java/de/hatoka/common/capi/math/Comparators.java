package de.hatoka.common.capi.math;

import java.util.Comparator;

public final class Comparators
{
    private Comparators()
    {
    }

    public static final Comparator<Object> NULL = new Comparator<Object>()
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            if (o1 == null)
            {
                if (o2 == null)
                {
                    return 0;
                }
                return -1;
            }
            if (o2 == null)
            {
                return 1;
            }
            return 0;
        }
    };

    public static final Comparator<Integer> INTEGER = new Comparator<Integer>()
    {
        @Override
        public int compare(Integer o1, Integer o2)
        {
            if (o1 == null || o2 == null)
            {
                return NULL.compare(o1, o2);
            }
            return o1 - o2;
        }
    };

    public static final Comparator<String> STRING = new Comparator<String>()
    {
        @Override
        public int compare(String o1, String o2)
        {
            if (o1 == null || o2 == null)
            {
                return NULL.compare(o1, o2);
            }
            return o1.compareTo(o2);
        }
    };

    public static final Comparator<Boolean> BOOLEAN = new Comparator<Boolean>()
    {
        @Override
        public int compare(Boolean o1, Boolean o2)
        {
            if (o1 == null || o2 == null)
            {
                return NULL.compare(o1, o2);
            }
            int result = 0;
            if (o1)
            {
                result += 1;
            }
            if (o2)
            {
                result -= 1;
            }
            return result;
        }
    };

    public static final Comparator<Money> MONEY = new Comparator<Money>()
    {
        @Override
        public int compare(Money o1, Money o2)
        {
            if (o1 == null || o2 == null)
            {
                return NULL.compare(o1, o2);
            }
            return o1.getAmount().compareTo(o2.getAmount());
        }
    };
}
