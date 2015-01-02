package de.hatoka.common.capi.dao.relations;

public abstract class ManyToOneEntityRelationUpdater<O, M extends EntityRelationMany<O>>
{
    private final O observed;
    private M container;

    /**
     *
     * @param observed
     * @param container current container
     */
    public ManyToOneEntityRelationUpdater(O observed, M container)
    {
        this.observed = observed;
        this.container = container;
    }

    public abstract void set(M newContainer);

    public void updateTo(M newContainer)
    {
        // test is equal
        if (newContainer == null)
        {
            if (container == null)
            {
                return;
            }
        }
        else if (newContainer.equals(container))
        {
            // nothing to
            return;
        }
        // important set before set of users is updated
        set(newContainer);
        M oldContainer = container;
        container = newContainer;
        // update old and new object
        if (oldContainer != null)
        {
            oldContainer.remove(observed);
        }
        if (container != null)
        {
            container.add(observed);
        }
    }

}
