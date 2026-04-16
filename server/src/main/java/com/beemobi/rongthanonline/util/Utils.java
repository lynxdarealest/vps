package com.beemobi.rongthanonline.util;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static final Random rand = new Random();

    public static Gson gson = new Gson();

    public static int nextInt(int max) {
        return rand.nextInt(max);
    }

    public static int nextInt(int min, int max) {
        if (min >= max) {
            return max;
        }
        return rand.nextInt(max + 1 - min) + min;

    }

    public static int nextArray(int[] array) {
        return array[Utils.nextInt(array.length)];
    }

    public static Player nextArray(List<Player> playerList) {
        if (playerList.isEmpty()) {
            return null;
        }
        return playerList.get(Utils.nextInt(playerList.size()));
    }

    public static Integer[] nextInt(Integer[] array, int count) {
        if (count >= array.length) {
            return array;
        }
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return Arrays.copyOfRange(array, 0, count);
    }

    public static long nextLong(long min, long max) {
        if (min >= max) {
            return max;
        }
        return min + (long) (Math.random() * (max - min));
    }

    public static boolean isPercent(int percent) {
        return nextInt(100) < percent;
    }

    public static long percentOf(long x, long p) {
        return x * p / 100;
    }

    public static long statsOfPoint(long stats, long point) {
        //return stats * point / (Math.abs(point) + 10000);
        return stats * getPercentPoint(point) / 100;
    }

    public static int upOptionParam(long param1, long param2) {
        if (param1 == 0) {
            return (int) param2;
        }
        if (param2 == 0) {
            return (int) param1;
        }
        long result = (10000 * (2 * param1 * param2 + 10000 * (param1 + param2))) / (10000 * 10000 - param1 * param2);
        return (int) result;
    }

    public static String pointToPercent(long point) {
        long num = 1000;
        double percent = 0;
        for (int i = 1; i <= 10; i++) {
            if (point <= num) {
                percent += (double) point * 10.0 / (double) num;
                break;
            }
            percent += 10;
            point -= num;
            num *= 2;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(percent) + "%";
        /*double number = (double) point / (point + 10000.0);
        DecimalFormat decimalFormat = new DecimalFormat("#.##%");
        decimalFormat.setDecimalSeparatorAlwaysShown(false);
        return decimalFormat.format(number);*/
    }

    public static void setTimeout(Runnable runnable, long delay) {
        new Thread(() -> {
            try {
                if (delay > 0) {
                    Thread.sleep(delay);
                }
                runnable.run();
            } catch (Exception ignored) {
            }
        }).start();
    }

    public static void run(Runnable runnable) {
        new Thread(() -> {
            try {
                runnable.run();
            } catch (Exception ignored) {
            }
        }).start();
    }

    public static void setScheduled(Runnable runnable, long intervalSecond, int hour, int minute) {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        if (zonedNow.getHour() == hour && zonedNow.getMinute() > minute) {
            runnable.run();
        }
        ZonedDateTime zonedNext = zonedNow.withHour(hour).withMinute(minute).withSecond(0);
        if (zonedNow.compareTo(zonedNext) > 0) {
            zonedNext = zonedNext.plusSeconds(intervalSecond);
        }
        Duration duration = Duration.between(zonedNow, zonedNext);
        long delay = duration.getSeconds();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(runnable, delay, intervalSecond, TimeUnit.SECONDS);
    }

    public static int getIndexDayOfWeek(long milliseconds) {
        Instant instant = Instant.ofEpochMilli(milliseconds);
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Ho_Chi_Minh"));
        return zdt.getDayOfWeek().getValue();
    }

    public static String formatTime(Timestamp time) {
        Date date = new Date(time.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        return formatter.format(date);
    }

    public static ZonedDateTime getLocalDateTime() {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
        return ZonedDateTime.of(localNow, currentZone);
    }

    public static int getWeekOfYearNow() {
        return LocalDateTime.now().get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    public static int getWeekOfYear(Timestamp timestamp) {
        return timestamp.toLocalDateTime().get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    public static int getLastWeekOfYear() {
        return LocalDateTime.now().minusWeeks(1).get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    public static int getDayOfWeekNow() {
        int day = LocalDateTime.now().getDayOfWeek().getValue();
        return day % 7 + 1;
    }

    public static long getStartOfDayMillis() {
        ZonedDateTime now = getLocalDateTime();
        return now.with(LocalTime.MIN).toInstant().toEpochMilli();
    }

    public static long getEndOfDayMillis() {
        ZonedDateTime now = getLocalDateTime();
        return now.with(LocalTime.MAX).toInstant().toEpochMilli();
    }

    public static long getStartOfWeekMillis() {
        ZonedDateTime now = getLocalDateTime();

        if (now.getDayOfWeek() == DayOfWeek.SUNDAY) {
            now = now.minusDays(1);
        }

        ZonedDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
        return startOfWeek.toInstant().toEpochMilli();
    }

    public static long getEndOfWeekMillis() {
        ZonedDateTime now = getLocalDateTime();

        if (now.getDayOfWeek() == DayOfWeek.SUNDAY) {
            now = now.minusDays(1);
        }

        ZonedDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);
        return endOfWeek.toInstant().toEpochMilli();
    }

    public static LocalDateTime getStartOfWeek(LocalDateTime dateRefer) {

        ZonedDateTime zoneDateTimeRefer = getZoneDateTimeRefer(dateRefer);

        if (zoneDateTimeRefer.getDayOfWeek() == DayOfWeek.SUNDAY) {
            zoneDateTimeRefer = zoneDateTimeRefer.minusDays(1);
        }

        ZonedDateTime startOfWeek = zoneDateTimeRefer.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
        return startOfWeek.toLocalDateTime();
    }

    public static LocalDateTime getEndOfWeek(LocalDateTime dateRefer) {

        ZonedDateTime zoneDateTimeRefer = getZoneDateTimeRefer(dateRefer);

        if (zoneDateTimeRefer.getDayOfWeek() == DayOfWeek.SUNDAY) {
            zoneDateTimeRefer = zoneDateTimeRefer.minusDays(1);
        }

        ZonedDateTime endOfWeek = zoneDateTimeRefer.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);
        return endOfWeek.toLocalDateTime();
    }

    public static ZonedDateTime getZoneDateTimeRefer(LocalDateTime referDate) {
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
        return ZonedDateTime.of(referDate, currentZone);
    }

    public static String getStringDateTimeFromMillis(long millis) {
        return getStringDateTimeFromMillis(millis, "HH:mm:ss dd-MM-yyyy");
    }

    public static String getStringDateTimeFromMillis(long millis, String pattern) {
        Date date = new Date(millis);

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(date);
    }

    public static boolean isInWeek(long referMillis) {
        long startOfWeek = getStartOfWeekMillis();
        long endOfWeek = getEndOfWeekMillis();

        return startOfWeek <= referMillis && endOfWeek >= referMillis;
    }

    public static int getYearNow() {
        return LocalDateTime.now().getYear();
    }

    public static int getCountDay(Timestamp date) {
        LocalDate localDate = date.toLocalDateTime().toLocalDate();
        LocalDate now = LocalDate.now();
        return (int) ChronoUnit.DAYS.between(localDate, now);
    }

    public static String getMoneys(long m) {
        String text = "";
        long num = m / 1000 + 1;
        for (int i = 0; i < num; i++) {
            if (m >= 1000) {
                long num2 = m % 1000;
                text = ((num2 != 0L) ? ((num2 >= 10) ? ((num2 >= 100) ? ("." + num2 + text) : (".0" + num2 + text)) : (".00" + num2 + text)) : (".000" + text));
                m /= 1000;
                continue;
            }
            text = m + text;
            break;
        }
        return text;
    }

    public static String currencyFormat(long m) {
        StringBuilder text = new StringBuilder();
        long num = m / 1000L + 1L;
        int num2 = 0;
        while ((long) num2 < num) {
            if (m < 1000L) {
                text.insert(0, m);
                break;
            }
            long num3 = m % 1000L;
            if (num3 == 0L) {
                text.insert(0, ".000");
            } else if (num3 < 10L) {
                text.insert(0, ".00" + num3);
            } else if (num3 < 100L) {
                text.insert(0, ".0" + num3);
            } else {
                text.insert(0, "." + num3);
            }
            m /= 1000L;
            num2++;
        }
        return text.toString();
    }

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String formatTime(long time) {
        if (time > 86400000) {
            return String.format("%d ngày", time / 86400000);
        }
        if (time > 3600000) {
            return String.format("%d giờ", time / 3600000);
        }
        if (time > 60000) {
            return String.format("%d phút", time / 60000);
        }
        return String.format("%d giây", time / 1000);
    }

    public static int[] formatPercent(long quantity, long total) {
        long t = quantity * 10000 / total;
        int[] p = new int[2];
        p[0] = (int) (t / 100);
        p[1] = (int) (t - (p[0] * 100));
        return p;
    }

    public static String formatNumber(long number) {
        String text = "";
        String text2 = "";
        if (number >= 1000000000000000L) {
            text2 = "Tr Tỉ";
            long num = number % 1000000000000000L / 10000000000000L;
            number /= 1000000000000000L;
            text = number + "";
            if (num >= 10L) {
                if (num % 10L == 0L) {
                    num /= 10L;
                }
                text = text + "," + num + text2;

            } else if (num > 0L) {
                text = text + ",0" + num + text2;
            } else {
                text += text2;
            }
        } else if (number >= 1000000000000L) {
            text2 = "K Tỉ";
            long num = number % 1000000000000L / 10000000000L;
            number /= 1000000000000L;
            text = number + "";
            if (num >= 10L) {
                if (num % 10L == 0L) {
                    num /= 10L;
                }
                text = text + "," + num + text2;

            } else if (num > 0L) {
                text = text + ",0" + num + text2;
            } else {
                text += text2;
            }
        } else if (number >= 1000000000L) {
            text2 = "Tỉ";
            long num = number % 1000000000L / 10000000L;
            number /= 1000000000L;
            text = number + "";
            if (num >= 10L) {
                if (num % 10L == 0L) {
                    num /= 10L;
                }
                text = text + "," + num + text2;

            } else if (num > 0L) {
                text = text + ",0" + num + text2;
            } else {
                text += text2;
            }
        } else if (number >= 1000000L) {
            text2 = "Tr";
            long num2 = number % 1000000L / 10000L;
            number /= 1000000L;
            text = number + "";
            if (num2 >= 10L) {
                if (num2 % 10L == 0L) {
                    num2 /= 10L;
                }
                text = text + "," + num2 + text2;
            } else if (num2 > 0L) {
                text = text + ",0" + num2 + text2;
            } else {
                text += text2;
            }
        } else if (number >= 10000L) {
            text2 = "K";
            long num3 = number % 1000L / 10L;
            number /= 1000L;
            text = number + "";
            if (num3 >= 10L) {
                if (num3 % 10L == 0L) {
                    num3 /= 10L;
                }
                text = text + "," + num3 + text2;
            } else if (num3 > 0L) {
                text = text + ",0" + num3 + text2;
            } else {
                text += text2;
            }
        } else if (number >= 1000L) {
            text2 = "K";
            long num3 = number % 1000L / 10L;
            number /= 1000L;
            text = number + "";
            if (num3 >= 10L) {
                if (num3 % 10L == 0L) {
                    num3 /= 10L;
                }
                text = text + "," + num3 + text2;
            } else if (num3 > 0L) {
                text = text + ",0" + num3 + text2;
            } else {
                text += text2;
            }
        } else {
            text = number + "";
        }
        return text;
    }

    public static int getDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static int getDistance(Entity entity1, Entity entity2) {
        return (int) Math.sqrt(Math.pow(entity2.x - entity1.x, 2) + Math.pow(entity2.y - entity1.y, 2));
    }

    public static String getTimeAgo(int timeRemainS) {
        int num = 0;
        if (timeRemainS > 60) {
            num = timeRemainS / 60;
            timeRemainS %= 60;
        }
        int num2 = 0;
        if (num > 60) {
            num2 = num / 60;
            num %= 60;
        }
        int num3 = 0;
        if (num2 > 24) {
            num3 = num2 / 24;
            num2 %= 24;
        }
        String text = "";
        if (num3 > 0) {
            text += num3;
            text += " ngày";
            text = text + num2 + " giờ";
        } else if (num2 > 0) {
            text += num2;
            text += " giờ";
            text = text + num + " phút";
        } else {
            if (num == 0) {
                num = 1;
            }
            text += num;
            text += " phút";
        }
        return text;
    }

    public static int getPercent(int point) {
        return point * 100 / (10000 + Math.abs(point));
    }

    public static String getJsonArrayItem(Item[] items) {
        JSONArray jsonArray = new JSONArray();
        for (Item item : items) {
            if (item != null) {
                jsonArray.put(item.toJsonObject());
            }
        }
        JSONObject object = new JSONObject();
        object.put("size", items.length);
        object.put("items", jsonArray);
        return object.toString();
    }

    public static String getJsonListItem(List<Item> items) {
        JSONArray jsonArray = new JSONArray();
        for (Item item : items) {
            if (item != null) {
                jsonArray.put(item.toJsonObject());
            }
        }
        return jsonArray.toString();
    }

    public static int getPercentPoint(long point) {
        long num = 1000;
        double percent = 0;
        for (int i = 1; i <= 10; i++) {
            if (point <= num) {
                percent += (double) point * 10.0 / (double) num;
                break;
            }
            percent += 10;
            point -= num;
            num *= 2;
        }
        return (int) percent;
    }

}
