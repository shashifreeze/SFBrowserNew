package in.blogspot.programmingdesire.sfbrowsernew.download;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import in.blogspot.programmingdesire.sfbrowsernew.R;
import in.blogspot.programmingdesire.sfbrowsernew.myutils.OpenFileByClick;

import static org.apache.commons.io.FileUtils.getExtension;


public class DownloadingFileAdapter extends RecyclerView.Adapter<DownloadingFileAdapter.DownloadingViewHolder> {

    private View view;
    private List<DownloadingFileModelClass> downloadList;
    private Fetch fetch;

    public DownloadingFileAdapter(List<DownloadingFileModelClass> downloadList) {
        this.downloadList = downloadList;
    }

    @NonNull
    @Override
    public DownloadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.download_row_item,parent,false);

        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(parent.getContext())
                .setDownloadConcurrentLimit(3)
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);

        return new DownloadingViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DownloadingViewHolder holder, int position) {


        String downloadUrl=downloadList.get(position).getUrl();
        String downloadFileName=downloadList.get(position).getFilename();
        String downloadFilePath=downloadList.get(position).getDownloadFilePath();

        holder.fileNameTV.setText(downloadFileName);

        final Request request = new Request(downloadUrl, downloadFilePath+"/"+downloadFileName);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);
        request.addHeader("clientKey", "SFBrowserByShashiKumar");

        fetch.enqueue(request, updatedRequest -> {
            //Request was successfully enqueued for download.
        }, error -> {
            //An error occurred enqueuing the request.
        });

        int downloadId=request.getId();

        FetchListener fetchListener = new FetchListener() {
            @Override
            public void onWaitingNetwork(@NotNull Download download) {
                if (request.getId() == download.getId()) {
                holder.pauseResume.setText("Waiting");}
            }


            @Override
            public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {

                if (request.getId() == download.getId()) {
                    holder.pauseResume.setText("Pause");
                    holder.cancelBtn.setText("Cancel");}
            }

            @Override
            public void onError(@NotNull Download download, @NotNull Error error, @Nullable Throwable throwable) {

                if (request.getId() == download.getId()) {
                    holder.pauseResume.setText("Retry");
                    holder.cancelBtn.setText("Error");
                }
            }

            @Override
            public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

            }

            @Override
            public void onAdded(@NotNull Download download) {

            }

            @Override
            public void onQueued(@NotNull Download download, boolean waitingOnNetwork) {
                if (request.getId() == download.getId()) {
                   // showDownloadInList(download);
                    holder.fileSizeTV.setText(download.getTotal()/(1024*1024)+"Mb");
                    holder.cancelBtn.setText("Quewed");
                    holder.pauseResume.setText("Waiting");
                }
            }

            @Override
            public void onCompleted(@NotNull Download download) {
                if (request.getId() == download.getId()) {
                    // updateDownload(download, etaInMilliSeconds);
                    long progressPercent =download.getProgress();
                    holder.downloading_perTV.setText(progressPercent+"%");
                    holder.download_time_leftTV.setVisibility(View.INVISIBLE);
                    holder.downloading_speedTV.setVisibility(View.INVISIBLE);
                    holder.progressBar.setProgress((int) progressPercent);
                    holder.progressBar.setIndeterminate(false);
                    holder.pauseResume.setText("Open");
                    holder.fileSizeTV.setText(download.getTotal()/(1024*1024)+"Mb");
                    holder.cancelBtn.setText("Completed");
                }
            }

            @Override
            public void onProgress(@NotNull Download download, long etaInMilliSeconds, long downloadedBytesPerSecond) {
                if (request.getId() == download.getId()) {
                   // updateDownload(download, etaInMilliSeconds);
                    if (downloadedBytesPerSecond>=(1024*1024))
                    {
                        holder.downloading_speedTV.setText(downloadedBytesPerSecond/(1024*1024)+" Mb/s");
                    }else
                    {
                        holder.downloading_speedTV.setText(downloadedBytesPerSecond/1024+" Kb/s");
                    }
                    long progressPercent =download.getProgress();
                    holder.downloading_perTV.setText(progressPercent+"%");
                    holder.download_time_leftTV.setText(etaInMilliSeconds/(1000*60)+" min left ");

                    holder.progressBar.setProgress((int) progressPercent);
                    holder.progressBar.setIndeterminate(false);

                    if (progressPercent==100)
                    {
                        Toast.makeText(holder.itemView.getContext(), "File donwloaded", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onPaused(@NotNull Download download) {
                if (request.getId() == download.getId()) {
                    holder.pauseResume.setText("Resume");
                }
            }

            @Override
            public void onResumed(@NotNull Download download) {
                if (request.getId() == download.getId()) {
                holder.pauseResume.setText("Pause");}
            }

            @Override
            public void onCancelled(@NotNull Download download) {
                if (request.getId() == download.getId()) {
                holder.pauseResume.setText("Cancelled");}
            }

            @Override
            public void onRemoved(@NotNull Download download) {

            }

            @Override
            public void onDeleted(@NotNull Download download) {

            }
        };

        fetch.addListener(fetchListener);

        holder.pauseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((TextView)v).getText().equals("Resume"))
                    fetch.resume(downloadId);
                else if (((TextView)v).getText().equals("Pause"))
                    fetch.pause(downloadId);
                else if (((TextView)v).getText().equals("Open")){

                    OpenFileByClick.openFile(v.getContext(),downloadFilePath+"/"+downloadFileName,getExtension(downloadFileName));
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return downloadList.size();
    }

     class DownloadingViewHolder extends RecyclerView.ViewHolder {

        TextView fileNameTV;
        ProgressBar progressBar;
        TextView fileSizeTV;
         TextView cancelBtn;
         TextView pauseResume,downloading_perTV,downloading_speedTV,download_time_leftTV;

        public DownloadingViewHolder(View itemView) {
            super(itemView);

            fileNameTV=itemView.findViewById(R.id.download_filenameTV);
            progressBar=itemView.findViewById(R.id.download_progressBar);
            fileSizeTV=itemView.findViewById(R.id.download_total_sizeTV);
            cancelBtn=itemView.findViewById(R.id.cancle_downloadBtn);
            pauseResume=itemView.findViewById(R.id.pauseResumeBtn);
            downloading_speedTV=itemView.findViewById(R.id.downloading_speedTV);
            downloading_perTV=itemView.findViewById(R.id.downloading_perTV);
            download_time_leftTV=itemView.findViewById(R.id.download_time_leftTV);
        }
    }
}
