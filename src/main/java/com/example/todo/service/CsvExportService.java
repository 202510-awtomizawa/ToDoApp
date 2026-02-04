package com.example.todo.service;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.todo.entity.Todo;

@Service
public class CsvExportService {
  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public void writeCsv(Writer writer, List<Todo> todos) throws IOException {
    writer.write(String.join(",",
        "ID", "タイトル", "説明", "優先度", "カテゴリ", "期限", "ステータス", "作成日"));
    writer.write("\n");

    for (Todo todo : todos) {
      String[] values = new String[] {
          safe(todo.getId()),
          safe(todo.getTitle()),
          safe(todo.getDetail()),
          safe(todo.getPriority() == null ? null : todo.getPriority().getDisplayName()),
          safe(todo.getCategory() == null ? null : todo.getCategory().getName()),
          safe(formatDate(todo.getDeadline())),
          safe(todo.isCompleted() ? "完了" : "未完了"),
          safe(formatDateTime(todo.getCreatedAt()))
      };
      writer.write(toCsvLine(values));
      writer.write("\n");
    }
  }

  private String toCsvLine(String[] values) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < values.length; i++) {
      if (i > 0) {
        sb.append(',');
      }
      sb.append(escape(values[i]));
    }
    return sb.toString();
  }

  private String escape(String value) {
    String v = value == null ? "" : value;
    boolean needsQuote = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
    if (v.contains("\"")) {
      v = v.replace("\"", "\"\"");
    }
    if (needsQuote) {
      v = "\"" + v + "\"";
    }
    return v;
  }

  private String safe(Object value) {
    return value == null ? "" : String.valueOf(value);
  }

  private String formatDateTime(LocalDateTime value) {
    return value == null ? "" : DATE_TIME_FORMAT.format(value);
  }

  private String formatDate(LocalDate value) {
    return value == null ? "" : DATE_FORMAT.format(value);
  }
}
