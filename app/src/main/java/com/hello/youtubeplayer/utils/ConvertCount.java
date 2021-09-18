package com.hello.youtubeplayer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.common.App;
import com.hello.youtubeplayer.data.network.model.ChannelYoutube;
import com.hello.youtubeplayer.data.network.model.VideoYoutube;

import java.time.OffsetDateTime;
import java.util.Calendar;

public class ConvertCount {

    public ConvertCount() {
    }

    @SuppressLint("SetTextI18n")
    public String convertLikeCount(String count) {
        if (count != null) {
            long likeCount = Integer.parseInt(count);
            if (likeCount < 1000) {
                return likeCount + "";
            } else if ((likeCount / 1000) < 1000 && (likeCount / 1000) > 0) {
                return ((likeCount) / 1000 + " K");
            } else if ((likeCount / 1000000) < 1000 && (likeCount / 1000000) > 0) {
                return ((likeCount) / 1000000 + " M");
            } else {
                return ((likeCount) / 1000000000 + " B");
            }
        }
        return count;
    }

    @SuppressLint("SetTextI18n")
    public void convertCommentCount(VideoYoutube videoYoutube, TextView textView, LinearLayout linearLayout) {
        long commentCount = Integer.parseInt(videoYoutube.getItems().get(0).getStatistics().getCommentCount());
        if (commentCount == 0) {
            linearLayout.setVisibility(View.GONE);
        } else {
            if (commentCount < 1000) {
                textView.setText(commentCount + "");
            } else if ((commentCount / 1000) < 1000 && (commentCount / 1000) > 0) {
                textView.setText((commentCount) / 1000 + "K");
            } else if ((commentCount / 1000000) < 1000 && (commentCount / 1000000) > 0) {
                textView.setText((commentCount) / 1000000 + "M");
            } else {
                textView.setText((commentCount) / 1000000000 + "B");
            }
        }
    }


    @SuppressLint("SetTextI18n")
    public String convertViewCount(Context context, String count) {
        long viewCount = Integer.parseInt(count);
        String viewCounts = null;
        if (viewCount >= 1000 && viewCount < 1000000) {
            viewCounts = viewCount / 1000 + " " + context.getString(R.string.view_count);
        }
        if (viewCount >= 1000000 && viewCount < 1000000000) {
            viewCounts = viewCount / 1000000 + " " + context.getString(R.string.view_count_m);
        }
        if (viewCount >= 1000000000) {
            viewCounts = viewCount / 1000000000 + " " + context.getString(R.string.view_count_b);
        }
        return viewCounts;
    }

    public String convertTime(Context context, VideoYoutube.ItemsBean item) {
        String time = null;
        Calendar today = Calendar.getInstance();
        String myDate = item.getSnippet().getPublishedAt();
        String inputModified = myDate.replace(" ", "T");
        int lengthOfAbbreviatedOffset = 3;
        if (inputModified.indexOf("+") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
            // If third character from end is a PLUS SIGN, append ':00'.
            inputModified = inputModified + ":00";
        }
        if (inputModified.indexOf("-") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
            // If third character from end is a PLUS SIGN, append ':00'.
            inputModified = inputModified + ":00";
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            OffsetDateTime odt = OffsetDateTime.parse(inputModified);
            long millis = odt
                    .toInstant().toEpochMilli();
            long diff = today.getTimeInMillis() - millis;
            long days = diff / (24 * 60 * 60 * 1000);
            long year = days / 365;
            long month = (days - year * 365) / 30;
            long day = (days - year * 365 - month * 30);
            long hour = diff / (60 * 60 * 1000) - year * 365 * 24 - month * 30 * 24 - day * 24;
            long minute = diff / (60 * 1000) - year * 365 * 24 * 60 - month * 30 * 24 * 60 - day * 24 * 60 - hour * 60;
            if (year > 0) {
                time = year + " " + context.getString(R.string.last_year);
                ;
            } else if (month > 0 && year == 0) {
                time = month + " " + context.getString(R.string.last_month);
                ;
            } else if (day > 0 && month == 0 && year == 0) {
                time = day + " " + context.getString(R.string.last_day);
                ;
            } else if (hour > 0 && day == 0 && month == 0 && year == 0) {
                time = hour + " " + context.getString(R.string.hours_ago);
            } else if (minute > 0 && hour == 0 && day == 0 && month == 0 && year == 0) {
                time = minute + " " + context.getString(R.string.minute_ago);
                ;
            }
        }
        return time;
    }

    public String convertDuration(String durations) {
        long time = Integer.parseInt(durations);
        String duration = "01:52:33";
        if (time > 0 && time < 60) {
            if (time < 10) {
                duration = "00:00:0" + time;
            } else {
                duration = "00:00:" + time;
            }
        }
        if (time >= 60 && time < 3600) {
            if ((time / 60) < 10) {
                if ((time % 60) < 10) {
                    duration = "00:0" + (time / 60) + ":0" + (time % 60);
                } else {
                    duration = "00:0" + (time / 60) + ":" + (time % 60);
                }
            } else {
                if ((time % 60) < 10) {
                    duration = "00:" + (time / 60) + ":0" + (time % 60);
                } else {
                    duration = "00:" + (time / 60) + ":" + (time % 60);
                }
            }
        }
        if (time >= 3600) {
            if ((time / 3600) < 10) {
                if (((time % 3600) / 60) < 10) {
                    if (((time % 3600) % 60) < 10) {
                        duration = "0" + (time / 3600) + ":0" + ((time % 3600) / 60) + ":0" + (((time % 3600) % 60));
                    } else {
                        duration = "0" + (time / 3600) + ":0" + ((time % 3600) / 60) + ":" + (((time % 3600) % 60));
                    }
                } else {
                    if (((time % 3600) % 60) < 10) {
                        duration = "0" + (time / 3600) + ":" + ((time % 3600) / 60) + ":0" + (((time % 3600) % 60));
                    } else {
                        duration = "0" + (time / 3600) + ":" + ((time % 3600) / 60) + ":" + (((time % 3600) % 60));
                    }
                }
            } else {
                if (((time % 3600) / 60) < 10) {
                    if (((time % 3600) % 60) < 10) {
                        duration = (time / 3600) + ":0" + ((time % 3600) / 60) + ":0" + (((time % 3600) % 60));
                    } else {
                        duration = (time / 3600) + ":0" + ((time % 3600) / 60) + ":" + (((time % 3600) % 60));
                    }
                } else {
                    if (((time % 3600) % 60) < 10) {
                        duration = (time / 3600) + ":" + ((time % 3600) / 60) + ":0" + (((time % 3600) % 60));
                    } else {
                        duration = (time / 3600) + ":" + ((time % 3600) / 60) + ":" + (((time % 3600) % 60));
                    }
                }
            }
        }
        return duration;
    }

    @SuppressLint("SetTextI18n")
    public void convertViewCountChannel(TextView textView, ChannelYoutube channelYoutube) {
        String time = null;
        Calendar today = Calendar.getInstance();
        String myDate = channelYoutube.getItems().get(0).getSnippet().getPublishedAt();
        String inputModified = myDate.replace(" ", "T");
        int lengthOfAbbreviatedOffset = 3;
        if (inputModified.indexOf("+") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
            // If third character from end is a PLUS SIGN, append ':00'.
            inputModified = inputModified + ":00";
        }
        if (inputModified.indexOf("-") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
            // If third character from end is a PLUS SIGN, append ':00'.
            inputModified = inputModified + ":00";
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            OffsetDateTime odt = OffsetDateTime.parse(inputModified);
            long millis = odt
                    .toInstant().toEpochMilli();
            long diff = today.getTimeInMillis() - millis;
            long days = diff / (24 * 60 * 60 * 1000);
            long year = days / 365;
            long month = (days - year * 365) / 30;
            long day = (days - year * 365 - month * 30);
            long hour = diff / (60 * 60 * 1000) - year * 365 * 24 - month * 30 * 24 - day * 24;
            long minute = diff / (60 * 1000) - year * 365 * 24 * 60 - month * 30 * 24 * 60 - day * 24 * 60 - hour * 60;
            if (year > 0) {
                time = year + App.getContext().getResources().getString(R.string.last_year);
                ;
            } else if (month > 0 && year == 0) {
                time = month + App.getContext().getResources().getString(R.string.last_month);
                ;
            } else if (day > 0 && month == 0 && year == 0) {
                time = day + App.getContext().getResources().getString(R.string.last_day);
                ;
            } else if (hour > 0 && day == 0 && month == 0 && year == 0) {
                time = hour + App.getContext().getResources().getString(R.string.hours_ago);
            } else if (minute > 0 && hour == 0 && day == 0 && month == 0 && year == 0) {
                time = minute + App.getContext().getResources().getString(R.string.minute_ago);

            }
            long viewCount = Long.parseLong(channelYoutube.getItems().get(0).getStatistics().getSubscriberCount());
            String viewCounts = null;
            if (viewCount >= 1000 && viewCount < 1000000) {
                viewCounts = viewCount / 1000 + App.getContext().getResources().getString(R.string.view_count);
            }
            if (viewCount >= 1000000 && viewCount < 1000000000) {
                viewCounts = viewCount / 1000000 + App.getContext().getResources().getString(R.string.view_count_m);
            }
            if (viewCount >= 1000000000) {
                viewCounts = viewCount / 1000000000 + App.getContext().getResources().getString(R.string.view_count_b);
            }
            textView.setText(viewCounts + "-" + time);
        }
    }
}
