package e.m7eds.myapplication.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import e.m7eds.myapplication.Client;

@Dao
public interface ClientDao {

    @Query("SELECT * FROM client")
    LiveData<List<Client>> loadAllClient();

    @Insert
    void insertClient(Client client);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateClient(Client client);

    @Delete
    void deleteClient(Client client);

    @Query("SELECT * FROM client WHERE id = :id")
    LiveData<Client> loadClientById(int id);

}
