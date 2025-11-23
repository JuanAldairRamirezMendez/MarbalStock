<#
  Script: build-and-test.ps1
  Uso: Ejecutar desde PowerShell en la raíz del proyecto:
    .\build-and-test.ps1

  Qué hace:
  - Compila todos los archivos `.java` bajo `src/` hacia `out/classes`
  - Ejecuta `modelo.ProductoTestRunner`
  - Sale con código 0 si pasa, 1 si fallan tests, >1 si falla compilación
#>

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
$srcDir = Join-Path $projectRoot 'src'
$outDir = Join-Path $projectRoot 'out\\classes'

Write-Host "Compilando fuentes desde: $srcDir"
if (Test-Path $outDir) { Remove-Item $outDir -Recurse -Force }
New-Item -ItemType Directory -Path $outDir -Force | Out-Null

$javaFiles = Get-ChildItem -Path $srcDir -Recurse -Filter *.java | ForEach-Object { $_.FullName }
if ($javaFiles.Count -eq 0) {
  Write-Error "No se encontraron archivos .java en $srcDir"
  exit 2
}

# Construir argumentos para javac de forma segura y ejecutar
$javacArgs = @('-d', $outDir) + $javaFiles
Write-Host "javac" ($javacArgs -join ' ')
& javac @javacArgs
if ($LASTEXITCODE -ne 0) {
  Write-Error "Compilación fallida (javac). Código: $LASTEXITCODE"
  exit $LASTEXITCODE
}

Write-Host "Ejecución de pruebas (ProductoTestRunner)..."
# Ejecutar la clase de pruebas
& java -cp $outDir modelo.ProductoTestRunner
$testExit = $LASTEXITCODE
if ($testExit -eq 0) { Write-Host "Pruebas OK" } else { Write-Error "Pruebas fallaron. Código: $testExit" }
exit $testExit
