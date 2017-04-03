package fr.inria.rsommerard.fougere.data;

import android.os.Build;
import android.support.compat.BuildConfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;


/**
 * Created by Romain on 17/08/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DataPoolTest {

    private DataPool dataPool;

    @Before
    public void setup() {
        this.dataPool = new DataPool(RuntimeEnvironment.application);
    }

    @Test
    public void getAllData() {
        Data data1 = DataProducer.produce();
        this.dataPool.insert(data1);

        Data data2 = DataProducer.produce();
        this.dataPool.insert(data2);

        Assert.assertEquals(2, this.dataPool.getAll().size());
    }

    @Test
    public void insertAValidData() {
        Data data = DataProducer.produce();
        this.dataPool.insert(data);

        Assert.assertEquals(1, this.dataPool.getAll().size());
    }

    @Test
    public void insertAValidDataTwoTimes() {
        Data data = DataProducer.produce();
        this.dataPool.insert(data);
        this.dataPool.insert(data);

        Assert.assertEquals(1, this.dataPool.getAll().size());
    }

    @Test
    public void insertTwoValidData() {
        Data data1 = DataProducer.produce();
        this.dataPool.insert(data1);

        Data data2 = DataProducer.produce();
        this.dataPool.insert(data2);

        Assert.assertEquals(2, this.dataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullId() {
        Data data1 = DataProducer.produce();
        this.dataPool.insert(data1);

        Data data2 = new Data(null, data1.getIdentifier(), data1.getContent(), data1.getTtl(),
                data1.getDisseminate(), data1.getSent());
        this.dataPool.insert(data2);

        Assert.assertEquals(1, this.dataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullIdentifier() {
        Data data = DataProducer.produce();
        Data data1 = new Data(null, null, data.getContent(), data.getTtl(), data.getDisseminate(),
                data.getSent());
        this.dataPool.insert(data1);

        Assert.assertEquals(0, this.dataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullContent() {
        Data data = DataProducer.produce();
        Data data1 = new Data(null, data.getIdentifier(), null, data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.dataPool.insert(data1);

        Assert.assertEquals(0, this.dataPool.getAll().size());
    }

    @Test
    public void insertADataWithNegativeTtl() {
        Data data = DataProducer.produce();
        Data data1 = new Data(null, data.getIdentifier(), data.getContent(), -1,
                data.getDisseminate(), data.getSent());
        this.dataPool.insert(data1);

        Assert.assertEquals(0, this.dataPool.getAll().size());
    }

    @Test
    public void insertADataWithNegativeDisseminate() {
        Data data = DataProducer.produce();
        Data data1 = new Data(null, data.getIdentifier(), data.getContent(), data.getTtl(), -1,
                data.getSent());
        this.dataPool.insert(data1);

        Assert.assertEquals(0, this.dataPool.getAll().size());
    }

    @Test
    public void insertADataWithNegativeSent() {
        Data data = DataProducer.produce();
        Data data1 = new Data(null, data.getIdentifier(), data.getContent(), data.getTtl(),
                data.getDisseminate(), -1);
        this.dataPool.insert(data1);

        Assert.assertEquals(0, this.dataPool.getAll().size());
    }

    @Test
    public void updateAData() {
        Data data = DataProducer.produce();
        this.dataPool.insert(data);

        data.setContent("Modified!");
        this.dataPool.update(data);

        Assert.assertEquals("Modified!", this.dataPool.getAll().get(0).getContent());
    }

    @Test
    public void deleteAValidData() {
        Data data = DataProducer.produce();
        this.dataPool.insert(data);

        this.dataPool.delete(data);
        Assert.assertEquals(0, this.dataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullId() {
        Data data = DataProducer.produce();
        this.dataPool.insert(data);

        Data data1 = new Data(null, data.getIdentifier(), data.getContent(), data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.dataPool.delete(data1);

        Assert.assertEquals(0, this.dataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullIdentifier() {
        Data data = DataProducer.produce();
        this.dataPool.insert(data);

        Data data1 = new Data(null, null, data.getContent(), data.getTtl(), data.getDisseminate(),
                data.getSent());
        this.dataPool.delete(data1);

        Assert.assertEquals(1, this.dataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullContent() {
        Data data = DataProducer.produce();
        this.dataPool.insert(data);

        Data data1 = new Data(null, data.getIdentifier(), null, data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.dataPool.delete(data1);

        Assert.assertEquals(0, this.dataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNegativeTtl() {
        Data data = DataProducer.produce();
        this.dataPool.insert(data);

        Data data1 = new Data(null, data.getIdentifier(), data.getContent(), -1,
                data.getDisseminate(), data.getSent());
        this.dataPool.delete(data1);

        Assert.assertEquals(0, this.dataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNegativeDisseminate() {
        Data data = DataProducer.produce();
        this.dataPool.insert(data);

        Data data1 = new Data(null, data.getIdentifier(), data.getContent(), data.getTtl(), -1,
                data.getSent());
        this.dataPool.delete(data1);

        Assert.assertEquals(0, this.dataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNegativeSent() {
        Data data = DataProducer.produce();
        this.dataPool.insert(data);

        Data data1 = new Data(null, data.getIdentifier(), data.getContent(), data.getTtl(),
                data.getDisseminate(), data.getSent());
        this.dataPool.delete(data1);

        Assert.assertEquals(0, this.dataPool.getAll().size());
    }
}
