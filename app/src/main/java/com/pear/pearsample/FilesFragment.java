package com.pear.pearsample;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.DragStartHelper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.List;

/**
 * Fragment responsible for listing cpoied files. Uses recycler view as a base of list.
 */
public class FilesFragment extends Fragment {

    private RecyclerView mFilesRecyclerView;
    private FilesAdapter mAdapter;
    private DataWorker dw;



    /**
     * Contructor
     * @param localDw Data worker link
     */
    public FilesFragment(DataWorker localDw) {
        // Required empty public constructor
        dw = localDw;
    }

    /**
     * Update UI method. Binds adapter to recycler view and update adapters main data.
     */
    public void updateUI()
    {
        LocalFilesList filesList = LocalFilesList.update(dw.getFilesList());
        List<LocalFile> localFiles = filesList.getLocalFiles();

        if(mAdapter == null){
            mAdapter = new FilesAdapter(localFiles);
            mFilesRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.dataUpdate(localFiles);
            mAdapter.notifyDataSetChanged();
        }
    }


    //---------- Overrides ------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        mFilesRecyclerView = (RecyclerView) view.findViewById(R.id.files_fragment_recyclerView);
        mFilesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mFilesRecyclerView);

        updateUI();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    /**
     * Swipe handler. onSwipe event is using
     */
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int i = viewHolder.getAdapterPosition();
            try {
                delete(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateUI();
        }
    };

    /**
     * Delete func. Not really cute, because best way is deleting using viewHolder functionality.
     * But with using adapter method works too.
     */
    private void delete(int adapterPosition) throws Exception {
        try {
           LocalFile file =  mAdapter.getElement(adapterPosition);
           dw.deleteFile(file.getName());
            LocalFilesList filesList = LocalFilesList.update(dw.getFilesList());
            List<LocalFile> localFiles = filesList.getLocalFiles();
        }
        catch (Exception ex)
        {throw ex; }

    }

    //---------- View Holder ----------------------------------------------------------------------

    /**
     * View holder of Recycler View. Made like subclass
     */
    private class FilesHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mFileTitle, mFilePath;

        private LocalFile mLocalFile;

        public FilesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_file, parent, false));
            itemView.setOnClickListener(this);

            mFileTitle = (TextView)itemView.findViewById(R.id.file_name);
            mFilePath = (TextView) itemView.findViewById(R.id.file_path);
            itemView.setOnClickListener(this);
        }

        public void bind (LocalFile localFile)
        {
            mLocalFile = localFile;
            mFileTitle.setText(mLocalFile.getName());
            mFilePath.setText(mLocalFile.getCopiedFrom());
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "Tapped on local file. Swipe to delete.", Toast.LENGTH_LONG).show();

        }
    }

    //---------- Adapter ---------------------------------------------------------------------------

    /**
     * Adapter of Recycler View.
     * Creates List of visible items and some other common functionality.
     */
    private class FilesAdapter extends RecyclerView.Adapter<FilesHolder> //implements ItemTouchHelperAdapter
    {
        private List<LocalFile> mFileList;

        public FilesAdapter(List<LocalFile> filesList) { mFileList = filesList; }

/*
        public boolean isLocalFilesEquals (List<LocalFile> newList)
        {
            return mFileList.equals(newList);
        }
*/

        public LocalFile getElement(int position) throws Exception
        {
            if(position <= mFileList.size())
                return mFileList.get(position);
            else throw new Exception("There is now element in this position");
        }

        /**
         * One of basics methods. Using for updating data, because major trigger for updating UI
         * is files availability in app folder. Every updationg of recycler view comes from this method.
         * @param dataList
         */
        public void dataUpdate(List<LocalFile> dataList)
        {mFileList = dataList; }

        @NonNull
        @Override
        public FilesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new FilesHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FilesHolder holder, int position) {
            LocalFile localFile = mFileList.get(position);
            holder.bind(localFile);
        }

        @Override
        public int getItemCount() {
            return mFileList.size();
        }

    }

}
