package com.odiaz.moneymanager.service;

import com.odiaz.moneymanager.dto.TransactionDTO;
import com.odiaz.moneymanager.dto.income.IncomeDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ExcelService {

    public <T extends TransactionDTO> byte[] writeTransactionsToExcel(List<T> transactions) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Usuarios");
            // encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("S.No");
            headerRow.createCell(1).setCellValue("Nombre");
            headerRow.createCell(2).setCellValue("Categoria");
            headerRow.createCell(3).setCellValue("Monto");
            headerRow.createCell(4).setCellValue("Fecha");

            IntStream.range(0, transactions.size())
                    .forEach( i -> {
                        TransactionDTO dto = transactions.get(i);
                        Row row = sheet.createRow(i+1);
                        row.createCell(0).setCellValue(i+1);
                        row.createCell(1).setCellValue(dto.getName());
                        row.createCell(2).setCellValue(dto.getCategoryId() != null ? dto.getName() : "N/A");
                        row.createCell(3).setCellValue(dto.getAmount().toString());
                        row.createCell(4).setCellValue(dto.getDate().toString());
                    });

            // Convertir workbook a bytes
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Ha ocurrido un error al generar el archivo");
        }

    }

}
