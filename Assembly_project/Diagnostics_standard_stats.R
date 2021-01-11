## Input:
##  1. "/workspace/hramzr/PacBio/fetreval-pacbio/fetreval.fasta.contigs.fasta" (Assembly file in FASTA format)

## Function:
## Generate basic stats (like N50), generate GC_distribution per length of scaffold and generate a distribution of width per genomic location.

## Output:
## 1. Table with basic stats (With stats like N50, L90, L50)
## 2. Plot with GC distribution versus length of scaffold.
## 3. Plot with width distribution versus genomic location.
---
title: "fetreval diagnostics assembly"
author: "Mike"
date: "14/02/2020"
output: github_document
---


library(Biostrings)
library(dplyr)
knitr::opts_chunk$set(echo = TRUE)

L10 <- function(csizes) {
  return(Lx(csizes = csizes, 10))
}
L50 <- function(csizes) {
  return(Lx(csizes = csizes, 50))
}
L90 <- function(csizes) {
  return(Lx(csizes = csizes, 90))
}
Lx <- function(csizes, i) {
  if (!is.numeric(csizes)) 
    stop("'csizes' must be a vector containing the contig sizes")
  if (!is.integer(csizes)) 
    csizes <- as.integer(csizes)
  decreasing_csizes <- base::sort(csizes, decreasing = TRUE)
  tmp <- cumsum(decreasing_csizes)
  total_size <- tmp[length(tmp)]
  j <- i / 100 
  Lx <- length(decreasing_csizes[which(tmp <= total_size*j)])
  return(Lx)
}

## --------------------------------- Denovo assembly contigs ---------------------------------

dna <- readDNAStringSet("/workspace/hramzr/PacBio/fetreval-pacbio/fetreval.fasta.contigs.fasta")
dna %>% head


### Basic stats

widths <- dna %>% width() %>% sort()
n50 <- widths %>% N50()
summary <- widths %>% summary()
bases <- alphabetFrequency(dna)
stats <- t(data.frame(Min = summary[["Min."]], N50 = widths %>% N50(), Max = summary[["Max."]], N = length(dna), Sum = widths %>% sum(), Definitive = sum(bases[,c('A','C','G','T')]), Ambiguous = sum(bases[,"N"]), L50 = L50(widths), L90 = L90(widths)))
knitr::kable(stats, col.names = c("value"))

### Distribution of widths

hist(log2(widths), xlim=c(10,25), breaks=200, xlab="log2(lengths)", main=paste("Denovo assembly contigs: ", length(dna), " contigs"))

### Per scaffold GC content

GC <- apply(bases, 1, function(x)100 * sum(x[c('C','G')])/sum(x[c('A','C','G','T')]))
cols <- suppressWarnings(densCols(GC, rowSums(bases[,1:4])))

plot(GC, rowSums(bases[,1:4]), pch=16, col=cols, cex=0.3, ylab="Scaffold length", main="denovo assembly contigs")



